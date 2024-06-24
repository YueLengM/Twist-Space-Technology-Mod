package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class PreciseHighEnergyPhotonicQuantumMasterRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        TwistSpaceTechnology.LOG.info("PreciseHighEnergyPhotonicQuantumMasterRecipePool loading recipes.");

        final IRecipeMap PhC = GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes;

        final Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");
        final ItemStack eternalSingularity = GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1);
        // region Space Wrapper

        // UEV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(16),
                ItemList.EnergisedTesseract.get(16),
                eternalSingularity,
                ItemList.Field_Generator_UEV.get(8),
                Laser_Lens_Special.get(16),
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 64), Materials.SuperconductorUEVBase.getMolten(16 * 144))
            .itemOutputs(SpaceWarper.get(1))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
            .eut(RECIPE_UIV)
            .duration(512 * 20)
            .addTo(PhC);

        // UIV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(12),
                ItemList.EnergisedTesseract.get(12),
                eternalSingularity,
                ItemList.Field_Generator_UIV.get(4),
                Laser_Lens_Special.get(8),
                GT_Utility.getIntegratedCircuit(11))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 32), Materials.SuperconductorUIVBase.getMolten(8 * 144))
            .itemOutputs(SpaceWarper.get(2))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
            .eut(RECIPE_UMV)
            .duration(256 * 20)
            .addTo(PhC);

        // UMV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(8),
                ItemList.EnergisedTesseract.get(8),
                eternalSingularity,
                ItemList.Field_Generator_UMV.get(2),
                Laser_Lens_Special.get(4),
                GT_Utility.getIntegratedCircuit(12))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 32), Materials.SuperconductorUMVBase.getMolten(4 * 144))
            .itemOutputs(SpaceWarper.get(4))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
            .eut(RECIPE_UXV)
            .duration(128 * 20)
            .addTo(PhC);

        // UXV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(4),
                ItemList.EnergisedTesseract.get(4),
                eternalSingularity,
                ItemList.Field_Generator_UXV.get(1),
                Laser_Lens_Special.get(2),
                GT_Utility.getIntegratedCircuit(13))
            .fluidInputs(
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64),
                MaterialsUEVplus.Time.getMolten(144 * 32),
                MaterialsUEVplus.Space.getMolten(144 * 32))
            .itemOutputs(SpaceWarper.get(128))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000 * 128))
            .eut(RECIPE_MAX)
            .duration(64 * 20)
            .addTo(PhC);
        // endregion

        // Optical SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Chip_Optical.get(1),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(16000))
            .itemOutputs(GTCMItemList.OpticalSOC.get(1), GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 1, 14))
            .fluidOutputs(Materials.Helium.getPlasma(4000))
            .outputChances(10000, 1)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(6144 * 20)
            .addTo(PhC);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                SpaceWarper.get(1),
                ItemList.Circuit_Chip_Optical.get(8),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(32000))
            .itemOutputs(
                GTCMItemList.OpticalSOC.get(32),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 8, 14))
            .fluidOutputs(Materials.Helium.getPlasma(8000))
            .outputChances(10000, 10)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(12288 * 20)
            .addTo(PhC);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                ParticleTrapTimeSpaceShield.get(1),
                ItemList.Circuit_Chip_Optical.get(2),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(16000))
            .itemOutputs(GTCMItemList.OpticalSOC.get(4), GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 1, 14))
            .fluidOutputs(Materials.Helium.getPlasma(4000))
            .outputChances(10000, 10)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(128 * 20)
            .addTo(PhC);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                ParticleTrapTimeSpaceShield.get(1),
                SpaceWarper.get(1),
                ItemList.Circuit_Chip_Optical.get(16),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(32000))
            .itemOutputs(
                GTCMItemList.OpticalSOC.get(64),
                GTCMItemList.OpticalSOC.get(64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 8, 14))
            .fluidOutputs(Materials.Helium.getPlasma(8000))
            .outputChances(10000, 10000, 100)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(64 * 20)
            .addTo(PhC);
        //
        // GT_Values.RA.stdBuilder()
        // .itemInputs(ItemList.Casing_Dim_Bridge.get(1))
        // .itemOutputs(GTCMItemList.HighDimensionalExtend.get(1))
        // .noFluidInputs()
        // .noFluidOutputs()
        // .eut(RECIPE_UXV)
        // .duration(20)
        // .addTo(PhC);

    }
}
