/* NFC Spy is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFC Spy is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.sinpo.nfcspy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.cardemulation.HostApduService;
import android.nfc.tech.IsoDep;
import android.os.Bundle;

import java.io.IOException;

@SuppressLint("NewApi")
public final class ServiceFull extends HostApduService implements
		ServiceFactory.SpyService {
	private ServiceAgent agent;

	@Override
	public void onCreate() {
		super.onCreate();
		agent = new ServiceAgent(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("----> anaysis in servicefull " + intent.toString() +"_"+ intent.getExtras().toString() + "__ \n"+intent.getExtras().getBundle("ACTION_NFC_ATTACH"));
		/*Tag tag = (Tag)(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)); //手动发送的信息也从此传递
		IsoDep isoDep = IsoDep.get(tag);
		byte[] bytes = new byte[]{(byte)0x00,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x10,(byte)0x02};
		try {
			isoDep.connect();
			bytes = isoDep.transceive(bytes);
			System.out.println("---> transeive ack " + Logger.toHexString(bytes,0,bytes.length));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("---> tag = " +  isoDep.getTag().getId() + "___" + Logger.toHexString(bytes,0,bytes.length));*/

		agent.handleIntent(intent);
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		agent.onDestroy();
		agent = null;
		super.onDestroy();
	}

	@Override
	public void onDeactivated(int reason) {
		if (DEACTIVATION_LINK_LOSS == reason)
			agent.onDeactivated(reason);
	}

	@Override
	public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
		return agent.processCommandApdu(commandApdu, extras);
	}

	@Override
	public void processResponseApdu(byte[] apdu) {
		sendResponseApdu(apdu);
	}
}
