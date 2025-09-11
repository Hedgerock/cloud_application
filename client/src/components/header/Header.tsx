import {Link} from "react-router-dom";
import './Header.css';
import {useAuth} from "../../hooks/auth/useAuth.ts";
import {Loading} from "../../hoc/Loading.tsx";
import {LoadingProgress} from "../loading/LoadingProgress.tsx";
import {AuthenticatedUser} from "../auth/authenticated_user/AuthenticatedUser.tsx";
import {AuthenticationBox} from "../auth/authentication_box/AuthenticationBox.tsx";

export const Header = () => {
    const { isAuthenticated, isLoading, user } = useAuth();

    return (
        <div className="header">
            <LoadingProgress isFetching={isLoading} />

            <h1 className="header__title">Header</h1>

            <ul className="header-list">
                <li className="header-list-item">
                    <Link
                        to={"/page"}
                        className="header-list-item__value"
                    >
                        Some other page
                    </Link>
                </li>
            </ul>

            <Loading isLoading={ isLoading }>
                { isAuthenticated && user
                    ? <AuthenticatedUser entity={ user } />
                    : <AuthenticationBox />
                }
            </Loading>
        </div>
    )
}