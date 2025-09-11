import {Link, Outlet, useLocation} from "react-router-dom";
import {AnimatedTitle} from "../../components/utility_components/animated_title/AnimatedTitle.tsx";
import {memo} from "react";

type AuthPaths = "login" | "register" | "confirmation_email" | "confirm"

const getCurrentTitle = (path: AuthPaths) => {
    switch (path) {
        case "confirmation_email":
            return "Confirmation";
        case "register":
            return "Registration";
        case "login":
            return "Login";
        case "confirm":
            return "Final confirm"
    }
}

export const AuthTemplate = memo(() => {
    const { pathname } = useLocation();

    const path = pathname.replace("/auth/", "");
    const title = getCurrentTitle(path as AuthPaths) + " page";

    return (
        <div className="auth-block">
            <Link to={ "/" } className="auth-block__link">Return</Link>
            <AnimatedTitle title={ title } />
            <Outlet />
        </div>
    )
})