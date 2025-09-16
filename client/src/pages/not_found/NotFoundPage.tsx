import {AppStructure} from "../../hoc/template/AppStructure.tsx";
import {Link} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch, RootState} from "../../redux/store/store.ts";
import {useEffect} from "react";
import {removeCurrentPath} from "../../redux/slices/pathSlice.ts";

const NotFoundPage = () => {
    const dispatch = useDispatch<AppDispatch>();
    const {currentPath} = useSelector((state: RootState) => state.navigation)

    useEffect(() => {
        dispatch(removeCurrentPath({ target: currentPath }));
    }, [dispatch, currentPath]);

    return (
        <AppStructure>
            <div>
                <h1>Page not found</h1>
                <Link to={ "/" } replace={true}>Return to the main page</Link>
            </div>
        </AppStructure>
    )
}

export default NotFoundPage