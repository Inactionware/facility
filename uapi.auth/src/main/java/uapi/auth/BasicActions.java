/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

/**
 * Define basic actions for generic resource.
 * All customized resource's action must support basic action.
 */
public class BasicActions {

    /**
     * The resource can be read
     */
    public static final int READ    = 0x01;

    /**
     * The resource can be modified
     */
    public static final int MODIFY  = 0x02;

    /**
     * The resource can be deleted
     */
    public static final int DELETE  = 0x04;

    private BasicActions() { }
}
