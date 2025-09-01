import {AppStructure} from "../components/template/AppStructure.tsx";
import {Link} from "react-router-dom";

export const NotFoundPage = () => {

    return (
        <AppStructure>
            <div>
                <h1>Page not found</h1>
                <Link to="/" replace={true}>Return to the main page</Link>
            </div>
        </AppStructure>
    )
}