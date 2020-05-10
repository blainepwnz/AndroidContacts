package com.tomash.androidcontacts.utils;

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copied from http://android.macpod.net/raw/android-6.0.1_r77/cts/tests/tests/telecom/src/android/telecom/cts/TestUtils.java
 */

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.telecom.PhoneAccountHandle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TelecomTestUtils {
    static final String TAG = "TelecomCTSTests";
    static final boolean HAS_TELECOM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    static final long WAIT_FOR_STATE_CHANGE_TIMEOUT_MS = 10000;

    // Non-final to allow modification by tests not in this package (e.g. permission-related
    // tests in the Telecom2 test package.
    public static String PACKAGE = "com.android.cts.telecom";
    public static final String COMPONENT = "android.telecom.cts.CtsConnectionService";
    public static final String REMOTE_COMPONENT = "android.telecom.cts.CtsRemoteConnectionService";
    public static final String ACCOUNT_ID = "xtstest_CALL_PROVIDER_ID";
    public static final String REMOTE_ACCOUNT_ID = "xtstest_REMOTE_CALL_PROVIDER_ID";

    public static final String ACCOUNT_LABEL = "CTSConnectionService";
    public static final String REMOTE_ACCOUNT_LABEL = "CTSRemoteConnectionService";

    private static final String COMMAND_SET_DEFAULT_DIALER = "telecom set-default-dialer ";

    private static final String COMMAND_GET_DEFAULT_DIALER = "telecom get-default-dialer";

    private static final String COMMAND_GET_SYSTEM_DIALER = "telecom get-system-dialer";

    private static final String COMMAND_ENABLE = "telecom set-phone-account-enabled ";

    private static final String COMMAND_REGISTER_SIM = "telecom register-sim-phone-account ";

    public static final String MERGE_CALLER_NAME = "calls-merged";
    public static final String SWAP_CALLER_NAME = "calls-swapped";

    public static boolean shouldTestTelecom(Context context) {
        if (!HAS_TELECOM) {
            return false;
        }
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
            pm.hasSystemFeature(PackageManager.FEATURE_CONNECTION_SERVICE);
    }

    public static String setDefaultDialer(Instrumentation instrumentation, String packageName)
        throws Exception {
        return executeShellCommand(instrumentation, COMMAND_SET_DEFAULT_DIALER + packageName);
    }

    public static String getDefaultDialer(Instrumentation instrumentation) throws Exception {
        return executeShellCommand(instrumentation, COMMAND_GET_DEFAULT_DIALER);
    }

    public static String getSystemDialer(Instrumentation instrumentation) throws Exception {
        return executeShellCommand(instrumentation, COMMAND_GET_SYSTEM_DIALER);
    }

    public static void enablePhoneAccount(Instrumentation instrumentation,
                                          PhoneAccountHandle handle) throws Exception {
        final ComponentName component = handle.getComponentName();
        executeShellCommand(instrumentation, COMMAND_ENABLE
            + component.getPackageName() + "/" + component.getClassName() + " "
            + handle.getId());
    }

    public static void registerSimPhoneAccount(Instrumentation instrumentation,
                                               PhoneAccountHandle handle, String label, String address) throws Exception {
        final ComponentName component = handle.getComponentName();
        executeShellCommand(instrumentation, COMMAND_REGISTER_SIM
            + component.getPackageName() + "/" + component.getClassName() + " "
            + handle.getId() + " " + label + " " + address);
    }

    /**
     * Executes the given shell command and returns the output in a string. Note that even
     * if we don't care about the output, we have to read the stream completely to make the
     * command execute.
     */
    public static String executeShellCommand(Instrumentation instrumentation,
                                             String command) throws Exception {
        final ParcelFileDescriptor pfd =
            instrumentation.getUiAutomation().executeShellCommand(command);
        BufferedReader br = null;
        try (InputStream in = new FileInputStream(pfd.getFileDescriptor())) {
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String str = null;
            StringBuilder out = new StringBuilder();
            while ((str = br.readLine()) != null) {
                out.append(str);
            }
            return out.toString();
        } finally {
            if (br != null) {
                closeQuietly(br);
            }
            closeQuietly(pfd);
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
