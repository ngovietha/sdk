/*
 * Created by NQC on 4/26/20 12:43 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "base")
public class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
