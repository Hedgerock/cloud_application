import * as React from "react";
import {type FC, memo} from "react";
import type {ICurrentEntity} from "../../../models/ICurrentEntity.ts";
import type {IAuthUser} from "../../../redux/api/authApi.ts";
import {useLogout} from "../../../hooks/auth/useLogout.ts";

export const AuthenticatedUser: FC<ICurrentEntity<IAuthUser>> = memo(({ entity: user }) => {
    const { logout } = useLogout();

    const handleLogout = async(e: React.FormEvent) => {
        e.preventDefault();
        await logout();
    }

    return (
        <div>
            <h3>{ user.username }</h3>
            <form onSubmit={ handleLogout }>
                <button type={"submit"}>Logout</button>
            </form>
        </div>
    )
})