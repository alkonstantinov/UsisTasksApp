package com.ipltd_bg.usistasksapp.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ipltd_bg.usistasksapp.R;

public class NFCActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    String[][] techList = new String[][]{new String[]{NfcA.class.getName(),
            NfcB.class.getName(), NfcF.class.getName(), NfcV.class.getName(),
            IsoDep.class.getName(), MifareClassic.class.getName(), NfcBarcode.class.getName(), NdefFormatable.class.getName(),
            MifareUltralight.class.getName(), Ndef.class.getName()}};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);



    }

    @Override
    protected void onResume() {
        Activity ctx = this;
        if (ctx != null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(ctx);
            if (nfcAdapter == null) {
                Toast.makeText(getBaseContext(), "Устройството не поддържа NFC", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, null);
                finish();

            } else if (!nfcAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Не е включен NFC", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, null);
                finish();
            } else {
                pendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx,
                        ctx.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                IntentFilter filter = new IntentFilter();
                filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
                filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
                filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
                filter.addAction(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);

                nfcAdapter.enableForegroundDispatch(ctx, pendingIntent,
                        new IntentFilter[]{filter}, this.techList);
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }


    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNewIntent(intent);
    }

    public void handleNewIntent(Intent intent) {
        try {
            Tag tagFound = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String tagId = bytesToHexString(tagFound.getId());
            String data = extractTagData(intent);
            Toast.makeText(getBaseContext(),data,Toast.LENGTH_LONG).show();
            //showScanResult(tagId + " " + data)
            // ;

            Intent result = new Intent();
//---set the data to pass back---
            result.setData(Uri.parse(tagId));
            setResult(RESULT_OK, result);
            finish();
        } catch (Exception e) {

        }
    }


    private String extractTagData(Intent intent) {
        try {
            NdefMessage msgs[] = null;
            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
            }
            String msg = "";
            if (msgs != null) {
                msg = new String(msgs[0].getRecords()[0].getPayload(), "UTF-8");
                return msg;
            }
        } catch (Exception e) {
        }
        return "";
    }



}
