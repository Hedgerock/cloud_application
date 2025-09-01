import {Link} from "react-router-dom";
import './Header.css';

export const Header = () => {

    return (
        <div className="header">
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

            <div className="header-authorization-box">
                <div className="authorization-buttons">
                    <button className="authorization-buttons__button">Sign in</button>
                    <span>/</span>
                    <button className="authorization-buttons__button">Sign up</button>
                </div>
            </div>
        </div>
    )
}