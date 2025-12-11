package de.blinkt.openvpn.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import de.blinkt.openvpn.core.IOpenVPNServiceInternal;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;

public class DisconnectVPN extends Activity implements ServiceConnection {
    private IOpenVPNServiceInternal mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, OpenVPNService.class);
        intent.setAction(OpenVPNService.START_SERVICE);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        mService = IOpenVPNServiceInternal.Stub.asInterface(service);
        try {
            mService.stopVPN(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        mService = null;
    }
}
