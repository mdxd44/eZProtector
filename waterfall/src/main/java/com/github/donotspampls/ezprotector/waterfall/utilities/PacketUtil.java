/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.utilities;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

@SuppressWarnings("UnstableApiUsage")
public class PacketUtil {

    public static byte[] getSchematicaPayload() {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeByte(0);
        output.writeBoolean(false);
        output.writeBoolean(false);
        output.writeBoolean(false);

        return output.toByteArray();
    }

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
