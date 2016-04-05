package com.spotapps.backup;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

/**
 * Created by tty on 6/10/2015.
 */
public class SpotAppsBackupAgent extends BackupAgent {

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        // TODO TALYAC
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        // TODO TALYAC
    }
}
