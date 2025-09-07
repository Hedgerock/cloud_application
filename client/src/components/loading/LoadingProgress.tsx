import {type FC, useEffect} from "react";
import NProgress from 'nprogress';

export const LoadingProgress: FC<{ isFetching: boolean }> = ({ isFetching }) => {

    useEffect(() => {
        if (isFetching) {
            NProgress.start();
        } else {
            NProgress.done();
        }
    }, [isFetching])

    return null;
}