/*
 * Copyright (C) 2012 - 2013 Niall 'Rivernile' Scott
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors or contributors be held liable for
 * any damages arising from the use of this software.
 *
 * The aforementioned copyright holder(s) hereby grant you a
 * non-transferrable right to use this software for any purpose (including
 * commercial applications), and to modify it and redistribute it, subject to
 * the following conditions:
 *
 *  1. This notice may not be removed or altered from any file it appears in.
 *
 *  2. Any modifications made to this software, except those defined in
 *     clause 3 of this agreement, must be released under this license, and
 *     the source code of any modifications must be made available on a
 *     publically accessible (and locateable) website, or sent to the
 *     original author of this software.
 *
 *  3. Software modifications that do not alter the functionality of the
 *     software but are simply adaptations to a specific environment are
 *     exempt from clause 2.
 */

package uk.org.rivernile.edinburghbustracker.android.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import uk.org.rivernile.edinburghbustracker.android.R;

/**
 * This DialogFragment alerts the user that they do not have a suitable barcode
 * scanning application installed. Clicking on the positive button will take the
 * user to the Google Play Store, and clicking the negative button will dismiss
 * the dialog.
 * 
 * @author Niall Scott
 */
public class InstallBarcodeScannerDialogFragment extends DialogFragment {
    
    private Callbacks callbacks;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        
        try {
            callbacks = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new IllegalStateException(activity.getClass().getName() +
                    " does not implement " + Callbacks.class.getName());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.barcodescannerdialog_title)
                .setCancelable(true)
                .setMessage(R.string.barcodescannerdialog_message)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int which) {
                        callbacks.onShowInstallBarcodeScanner();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setInverseBackgroundForced(true);

        return builder.create();
    }
    
    /**
     * Any Activities which host this Fragment must implement this interface to
     * handle navigation events.
     */
    public static interface Callbacks {
        
        /**
         * This is called when the user wants to install a barcode scanner.
         */
        public void onShowInstallBarcodeScanner();
    }
}