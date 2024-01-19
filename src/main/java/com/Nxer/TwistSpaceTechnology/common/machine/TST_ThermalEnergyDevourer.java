package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.MAR_Casing;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;
import gregtech.common.blocks.GT_Block_Casings8;

public class TST_ThermalEnergyDevourer extends GTCM_MultiMachineBase<TST_ThermalEnergyDevourer> {

    // region Class Constructor
    public TST_ThermalEnergyDevourer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_ThermalEnergyDevourer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ThermalEnergyDevourer(this.mName);
    }
    // endregion

    // region Processing Logic

    /**
     * 0 = high speed mode
     * <p>
     * 1 = high parallel mode
     */
    private byte mode = ValueEnum.Mode_Default_ThermalEnergyDevourer;
    private int coefficientMultiplier = 1;

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA
            + texter("Coefficient Multiplier", "MachineInfoData.ThermalEnergyDevourer.coefficientMultiplier")
            + ": "
            + EnumChatFormatting.GOLD
            + this.coefficientMultiplier;
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", mode);
        aNBT.setInteger("coefficientMultiplier", coefficientMultiplier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("ThermalEnergyDevourer.modeMsg." + this.mode));
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getEuModifier() {
        return mode == 0 ? 1 : 1F / coefficientMultiplier;
    }

    @Override
    protected float getSpeedBonus() {
        return mode == 1 ? 1 : 0.5F / coefficientMultiplier;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return mode == 1 ? ValueEnum.Parallel_HighParallelMode_ThermalEnergyDevourer
            : ValueEnum.Parallel_HighSpeedMode_ThermalEnergyDevourer;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        coefficientMultiplier = 1 + getExtraCoefficientMultiplierByVoltageTier();
        return true;
    }

    public int getExtraCoefficientMultiplierByVoltageTier() {
        return (int) Utils.calculatePowerTier(getMaxInputEu());
    }
    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 36;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainThermalEnergyDevourer";
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }
    @Override
    public IStructureDefinition<TST_ThermalEnergyDevourer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

/*
Blocks:
A -> ofBlock...(MAR_Casing, 0, ...);
B -> ofBlock...(gt.blockcasings, 11, ...);
C -> ofBlock...(gt.blockcasings2, 1, ...); // io hatches
D -> ofBlock...(gt.blockcasings2, 8, ...);
E -> ofBlock...(gt.blockcasings8, 3, ...); // energy hatch
F -> ofFrame...(Materials.NaquadahAlloy);
 */
    private final IStructureDefinition<TST_ThermalEnergyDevourer> STRUCTURE_DEFINITION =
    StructureDefinition
        .<TST_ThermalEnergyDevourer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
            {"     CCCCC     ","   CCEEEEECC   ","  CEEEEEEEEEC  "," CEEEEEEEEEEEC "," CEEEEEEEEEEEC ","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC"," CEEEEEEEEEEEC "," CEEEEEEEEEEEC ","  CEEEEEEEEEC  ","   CCEEEEECC   ","     CCCCC     "},
            {"               ","       B       ","       B       ","       B       ","     FFBFF     ","    FFADAFF    ","    FA F AF    "," BBBBDF FDBBBB ","    FA F AF    ","    FFADAFF    ","     FFBFF     ","       B       ","       B       ","       B       ","               "},
            {"               ","               ","       B       ","       B       ","       B       ","      ADA      ","     A F A     ","  BBBDF FDBBB  ","     A F A     ","      ADA      ","       B       ","       B       ","       B       ","               ","               "},
            {"               ","               ","               ","       B       ","       B       ","      ADA      ","     A F A     ","   BBDF FDBB   ","     A F A     ","      ADA      ","       B       ","       B       ","               ","               ","               "},
            {"               ","               ","               ","       B       ","       B       ","      ADA      ","     A F A     ","   BBDF FDBB   ","     A F A     ","      ADA      ","       B       ","       B       ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","       D       ","       F       ","     DF FD     ","       F       ","       D       ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","       F       ","      F F      ","       F       ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","       F       ","      F F      ","       F       ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","      FFF      ","      F F      ","      FFF      ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","      F F      ","      F F      ","    FF D FF    ","      DDD      ","    FF D FF    ","      F F      ","      F F      ","               ","               ","               ","               "},
            {"               ","               ","               ","      F F      ","       D       ","       D       ","   F       F   ","    DD   DD    ","   F       F   ","       D       ","       D       ","      F F      ","               ","               ","               "},
            {"               ","               ","      F F      ","       D       ","               ","               ","  F         F  ","   D       D   ","  F         F  ","               ","               ","       D       ","      F F      ","               ","               "},
            {"               ","      F F      ","       D       ","               ","               ","               "," F           F ","  D         D  "," F           F ","               ","               ","               ","       D       ","      F F      ","               "},
            {"               ","      F F      ","       D       ","               ","               ","               "," F           F ","  D         D  "," F           F ","               ","               ","               ","       D       ","      F F      ","               "},
            {"      FFF      ","    FF D FF    ","   F       F   ","  F         F  "," F           F "," F           F ","F             F","FD           DF","F             F"," F           F "," F           F ","  F         F  ","   F       F   ","    FF D FF    ","      FFF      "},
            {"      F F      ","      DDD      ","    DD   DD    ","   D       D   ","  D         D  ","  D         D  ","FD           DF"," D           D ","FD           DF","  D         D  ","  D         D  ","   D       D   ","    DD   DD    ","      DDD      ","      F F      "},
            {"      FFF      ","    FF D FF    ","   F       F   ","  F         F  "," F           F "," F           F ","F             F","FD           DF","F             F"," F           F "," F           F ","  F         F  ","   F       F   ","    FF D FF    ","      FFF      "},
            {"               ","      F F      ","       D       ","               ","               ","               "," F           F ","  D         D  "," F           F ","               ","               ","               ","       D       ","      F F      ","               "},
            {"               ","      F F      ","       D       ","               ","               ","               "," F           F ","  D         D  "," F           F ","               ","               ","               ","       D       ","      F F      ","               "},
            {"               ","               ","      F F      ","       D       ","               ","               ","  F         F  ","   D       D   ","  F         F  ","               ","               ","       D       ","      F F      ","               ","               "},
            {"               ","               ","               ","      F F      ","       D       ","       D       ","   F       F   ","    DD   DD    ","   F       F   ","       D       ","       D       ","      F F      ","               ","               ","               "},
            {"               ","               ","               ","               ","      F F      ","      F F      ","    FF D FF    ","      DDD      ","    FF D FF    ","      F F      ","      F F      ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","      FFF      ","      F F      ","      FFF      ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","       F       ","      F F      ","       F       ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","       F       ","      F F      ","       F       ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","       D       ","       F       ","     DF FD     ","       F       ","       D       ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","       B       ","      ADA      ","     A F A     ","    BDF FDB    ","     A F A     ","      ADA      ","       B       ","               ","               ","               ","               "},
            {"               ","               ","               ","       B       ","       B       ","      ADA      ","     A F A     ","   BBDF FDBB   ","     A F A     ","      ADA      ","       B       ","       B       ","               ","               ","               "},
            {"               ","               ","               ","       B       ","       B       ","      ADA      ","     A F A     ","   BBDF FDBB   ","     A F A     ","      ADA      ","       B       ","       B       ","               ","               ","               "},
            {"               ","               ","       B       ","       B       ","       B       ","      ADA      ","     A F A     ","  BBBDF FDBBB  ","     A F A     ","      ADA      ","       B       ","       B       ","       B       ","               ","               "},
            {"               ","       B       ","       B       ","       B       ","     FFBFF     ","    FFADAFF    ","    FA F AF    "," BBBBDF FDBBBB ","    FA F AF    ","    FFADAFF    ","     FFBFF     ","       B       ","       B       ","       B       ","               "},
            {"     CC~CC     ","   CCEEEEECC   ","  CEEEEEEEEEC  "," CEEEEEEEEEEEC "," CEEEEEEEEEEEC ","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC","CEEEEEEEEEEEEEC"," CEEEEEEEEEEEC "," CEEEEEEEEEEEC ","  CEEEEEEEEEC  ","   CCEEEEECC   ","     CCCCC     "}
        }))
        .addElement('A', ofBlock(MAR_Casing,0))
        .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 11))
        .addElement(
            'C',
            GT_HatchElementBuilder
                .<TST_ThermalEnergyDevourer>builder()
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                .adder(TST_ThermalEnergyDevourer::addToMachineList)
                .dot(1)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(1))
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings2, 1)))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 8))
        .addElement(
            'E',
            GT_HatchElementBuilder
                .<TST_ThermalEnergyDevourer>builder()
                .atLeast(Energy.or(ExoticEnergy))
                .adder(TST_ThermalEnergyDevourer::addToMachineList)
                .dot(2)
                .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(3))
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings8, 3)))
        .addElement('F', ofFrame(Materials.NaquadahAlloy))
        .build();
    // spotless:on
    // endregion

    // region General
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_ThermalEnergyDevourer_MachineType)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_Controller)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_01)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_02)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_03)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_04)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_05)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_06)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_07)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_08)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_09)
            .addInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_10)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(15, 37, 15, false)
            .addController(TextLocalization.textFrontBottom)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { casingTexturePages[0][17] };
        }
        return rTexture;
    }
}
