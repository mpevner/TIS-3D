package li.cil.tis3d.system.module.execution;

import li.cil.tis3d.api.Port;
import li.cil.tis3d.system.module.execution.instruction.Instruction;
import li.cil.tis3d.system.module.execution.compiler.ParseException;
import li.cil.tis3d.system.module.execution.compiler.Compiler;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Virtual machine state for executing TIS-100 assembly.
 */
public final class MachineState {
    // --------------------------------------------------------------------- //
    // Persisted data

    /**
     * Program counter, i.e. the index of the next operation to execute.
     */
    public int pc = 0;

    /**
     * Accumulator register.
     */
    public int acc = 0;

    /**
     * Backup register.
     */
    public int bak = 0;

    /**
     * The port last read from.
     */
    public Optional<Port> last = Optional.empty();

    /**
     * Lines of original code this state was compiled from.
     */
    public String[] code;

    // --------------------------------------------------------------------- //
    // Computed data

    /**
     * The maximum value that can be stored in a register.
     */
    public static final int MAX_VALUE = 999;

    /**
     * List of instructions (the program) stored in the machine.
     */
    public final List<Instruction> instructions = new ArrayList<>(15);

    /**
     * List of labels and associated addresses.
     */
    public final HashMap<String, Integer> labels = new HashMap<>();

    /**
     * Instruction address to line number mapping.
     */
    public final HashMap<Integer, Integer> lineNumbers = new HashMap<>();

    // --------------------------------------------------------------------- //

    /**
     * Ensure values of the state are valid ones.
     */
    public void validate() {
        // Set to zero even when running out at the end to have programs
        // restart automatically.
        if (pc < 0 || pc >= instructions.size()) {
            pc = 0;
        }
        acc = Math.max(-MAX_VALUE, Math.min(MAX_VALUE, acc));
        bak = Math.max(-MAX_VALUE, Math.min(MAX_VALUE, bak));
    }

    /**
     * Soft reset the machine state.
     */
    public void reset() {
        pc = 0;
        acc = 0;
        bak = 0;
        last = Optional.empty();
    }

    /**
     * Hard reset the machine state.
     */
    public void clear() {
        reset();

        instructions.clear();
        labels.clear();
        code = null;
        lineNumbers.clear();
    }

    // --------------------------------------------------------------------- //

    public void readFromNBT(final NBTTagCompound nbt) {
        pc = nbt.getInteger("pc");
        acc = nbt.getInteger("acc");
        bak = nbt.getInteger("bak");
        if (nbt.hasKey("last")) {
            last = Optional.of(Port.valueOf(nbt.getString("last")));
        }

        validate();

        if (nbt.hasKey("code")) {
            try {
                Compiler.compile(nbt.getString("code"), this);
            } catch (final ParseException e) {
                // TODO logging
                e.printStackTrace();
            }
        }
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("pc", pc);
        nbt.setInteger("acc", acc);
        nbt.setInteger("bak", bak);
        last.ifPresent(port -> nbt.setString("last", port.name()));

        if (code != null) {
            nbt.setString("code", String.join("\n", code));
        }
    }
}
