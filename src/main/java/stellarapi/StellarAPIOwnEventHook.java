package stellarapi;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.impl.DefaultCollectionVanilla;
import stellarapi.impl.DefaultCoordinateVanilla;
import stellarapi.impl.DefaultSkyVanilla;

public class StellarAPIOwnEventHook {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onConstructCelestials(ConstructCelestialsEvent event) {
		if (event.getCollections().isEmpty()) {
			if (this.isOverworld(event.getWorld())) {
				DefaultCollectionVanilla collection = new DefaultCollectionVanilla(event.getWorld());
				event.getCollections().add(collection);
				event.getEffectors(IEffectorType.Light).add(collection.sun);
				event.getEffectors(IEffectorType.Tide).add(collection.moon);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onResetCoordinate(SetCoordinateEvent event) {
		if (event.getCoordinate() == null)
			event.setCoordinate(new DefaultCoordinateVanilla(event.getWorld()));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onResetEffect(SetSkyEffectEvent event) {
		if (event.getSkyEffect() == null)
			if (event.getWorld().provider.hasSkyLight())
				event.setSkyEffect(new DefaultSkyVanilla());
	}

	private boolean isOverworld(World world) {
		return world.provider.getDimension() == 0 && world.provider.isSurfaceWorld()
				&& (world.getCelestialAngle(0.5f) * 2) % 1.0f != 0.0f;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateScope(UpdateScopeEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack itemToCheck = viewer.getActiveItemStack();
			

			if (itemToCheck != null && itemToCheck.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemToCheck.getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isScope())
					event.setScope(property.getScope(viewer));
			}

			if (viewer.getRidingEntity() != null && viewer.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = viewer.getRidingEntity().getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isScope())
					event.setScope(property.getScope(viewer));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateFilter(UpdateFilterEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack itemToCheck = viewer.getActiveItemStack();

			if (itemToCheck != null && itemToCheck.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemToCheck.getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isFilter())
					event.setFilter(property.getFilter(viewer));
			}

			if (viewer.getRidingEntity() != null && viewer.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = viewer.getRidingEntity().getCapability(
						SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				if(property.isFilter())
					event.setFilter(property.getFilter(viewer));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkOpticalEntity(CheckEntityOpticalViewerEvent event) {
		// TODO better implementation & logic of checking for the optical entities.
		event.setIsOpticalEntity(event.getEntity() instanceof EntityPlayer);
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public void onClientWorldLoadedPre(ClientWorldEvent.Loaded event) {
		IProgressUpdate progress = event.getProgressUpdate("StellarAPI");
		progress.resetProgressAndMessage(I18n.format("progress.stellarapi.pending.main", event.getAttemptNumber()));
		progress.displayLoadingString(I18n.format("progress.stellarapi.pending.text", event.getAttemptNumber()));
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
	public void onClientWorldLoadedPost(ClientWorldEvent.Loaded event) {
		IProgressUpdate progress = event.getProgressUpdate("StellarAPI");
		if (event.isCanceled()) {
			progress.displayLoadingString(I18n.format("progress.stellarapi.other.pending.text"));
		} else
			progress.displayLoadingString("");
	}
}
