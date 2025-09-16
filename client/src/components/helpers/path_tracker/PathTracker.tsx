import {useLocation, useNavigationType} from "react-router-dom";
import {useEffect, useRef} from "react";
import {useDispatch} from "react-redux";
import type {AppDispatch} from "../../../redux/store/store.ts";
import {setPrevPath} from "../../../redux/slices/pathSlice.ts";

export const PathTracker = () => {
    const { pathname } = useLocation();
    const prevPathRef = useRef<string>("/");
    const pathHistory =  useRef<string[]>([]);
    const dispatch = useDispatch<AppDispatch>();
    const navigationType = useNavigationType();

    useEffect(() => {
        if (navigationType === "PUSH") {

            if (prevPathRef.current) {
                pathHistory.current.push(prevPathRef.current);
            }

            dispatch(setPrevPath(
                {
                    prevPath: prevPathRef.current,
                    currentPath: pathname,
                    pathHistory: [...pathHistory.current]
                }
            ))

            prevPathRef.current = pathname;
        }
    }, [dispatch, pathname, navigationType]);

    return null;
}