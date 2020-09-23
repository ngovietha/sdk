/*
 * Created by NQC on 4/25/20 8:05 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.local;


import com.nqc.idoctor.common.data.entity.BaseEntity;
import com.nqc.idoctor.common.data.local.converter.Converters;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Database app
 */
@Database(entities = {BaseEntity.class},
        version = 1, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class BaseDatabase extends RoomDatabase {

}
