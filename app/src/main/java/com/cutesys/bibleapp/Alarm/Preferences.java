/**************************************************************************
 *
 * Copyright (C) 2012-2015 Alex Taradov <alex@taradov.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *************************************************************************/

package com.cutesys.bibleapp.Alarm;
 
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.cutesys.bibleapp.R;

public class Preferences extends PreferenceActivity
{
  @Override
  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    addPreferencesFromResource(R.xml.preferences);
  }
  public void onBackPressed() {
    //finish();
    Intent i = new Intent (getApplicationContext(),AlarmMe.class);
    startActivity(i);
    overridePendingTransition(R.anim.bottom_up,
            android.R.anim.fade_out);
    finish();
  }
}

