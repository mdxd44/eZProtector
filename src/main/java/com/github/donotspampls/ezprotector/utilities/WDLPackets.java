package com.github.donotspampls.ezprotector.utilities;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

@SuppressWarnings("UnstableApiUsage")
public class WDLPackets {

    public static byte[] createWDLPacket0() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeInt(0);
        output.writeBoolean(false); // no new features allowed
        return output.toByteArray();
    }

    public static byte[] createWDLPacket1() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeInt(1);

        // no WDL features at all allowed
        output.writeBoolean(false);
        output.writeInt(0);
        output.writeBoolean(false);
        output.writeBoolean(false);
        output.writeBoolean(false);
        output.writeBoolean(false);

        return output.toByteArray();
    }

}
