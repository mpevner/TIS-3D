package li.cil.tis3d.api;

import li.cil.tis3d.api.module.Module;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * A casing for TIS-3D modules.
 * <p>
 * This is implemented by the tile entity of TIS-3D casings.
 */
public interface Casing {
    /**
     * The world this casing resides in.
     *
     * @return the world the casing lives in.
     */
    World getWorld();

    /**
     * The position of the casing in the world it exists in.
     *
     * @return the position of the casing.
     */
    BlockPos getPosition();

    /**
     * Flag the casing as dirty so it is saved when the chunk containing it saved next.
     */
    void markDirty();

    /**
     * Get the module installed on the specified face of the casing.
     *
     * @param face the face to get the module for.
     * @return the module installed on that face, or <tt>null</tt>.
     */
    Module getModule(Face face);

    /**
     * Set the module for the specified face of the casing.
     * <p>
     * This is automatically called by the casing tile entity when items are
     * added or removed, you'll usually not need to call this directly.
     *
     * @param face   the face to install the module on.
     * @param module the module to install on the face, or <tt>null</tt> for none.
     */
    void setModule(Face face, Module module);

    /**
     * Get the receiving pipe on the specified port of a module in this casing.
     * <p>
     * There are two {@link Pipe}s between every pair of {@link Module}s
     * in a case. Specifically, each edge of a {@link Casing} has two
     * {@link Pipe}s, going into opposite directions. This method is used
     * to to get a {@link Pipe} based on its sink.
     *
     * @param face the face to get the port for.
     * @param port the port for which to get the port.
     * @return the input port on that port.
     */
    Pipe getReceivingPipe(Face face, Port port);

    /**
     * Get the sending pipe on the specified port of a module in this casing.
     * <p>
     * There are two {@link Pipe}s between every pair of {@link Module}s
     * in a case. Specifically, each edge of a {@link Casing} has two
     * {@link Pipe}s, going into opposite directions. This method is used
     * to to get a {@link Pipe} based on its source.
     *
     * @param face the face to get the port for.
     * @param port the port for which to get the port.
     * @return the output port on that port.
     */
    Pipe getSendingPipe(Face face, Port port);

    /**
     * Call this to send some data to a client representation of a module.
     *
     * @param face the face the module is installed in.
     * @param data the data to send to the client.
     */
    void sendData(Face face, NBTTagCompound data);
}
