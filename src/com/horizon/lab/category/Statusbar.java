/*
 * Copyright (C) 2020 Project-Awaken
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
 */
package com.horizon.lab.category;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.custom.preference.SystemSettingListPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.horizon.lab.utils.TelephonyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class Statusbar extends SettingsPreferenceFragment  {

    private static final String KEY_SHOW_FOURG = "show_fourg_icon";

    private SwitchPreference mShowFourg;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.category_statusbar);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

        mShowFourg = (SwitchPreference) findPreference(KEY_SHOW_FOURG);

        if (!TelephonyUtils.isVoiceCapable(getActivity())) {
            prefSet.removePreference(mShowFourg);
        }
    }


    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();

        Settings.System.putIntForUser(resolver,
                Settings.System.WIFI_STANDARD_ICON, 0, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                 Settings.Secure.ENABLE_CAMERA_PRIVACY_INDICATOR, 1, UserHandle.USER_CURRENT);
        Settings.Secure.putIntForUser(resolver,
                 Settings.Secure.ENABLE_LOCATION_PRIVACY_INDICATOR, 1, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HORIZON;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.category_statusbar;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);

                    if (!TelephonyUtils.isVoiceCapable(context)) {
                        keys.add(KEY_SHOW_FOURG);
                    }

                    return keys;
                }
            };
}
