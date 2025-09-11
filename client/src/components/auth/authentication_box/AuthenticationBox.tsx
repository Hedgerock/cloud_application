import {Link} from "react-router-dom";

export const AuthenticationBox = () => {

    return (
        <div className="header-authorization-box">
            <div className="authorization-buttons">
                <Link to={"/auth/login"} className="authorization-buttons__button">Sign in</Link>
                <span>/</span>
                <Link to={"/auth/register"} className="authorization-buttons__button">Sign up</Link>
            </div>
        </div>
    )
}