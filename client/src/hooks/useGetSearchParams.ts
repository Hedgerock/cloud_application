import {useSearchParams} from "react-router-dom";

export const useGetSearchParams = (key: string) => {
    const [searchParams] = useSearchParams();

    return searchParams.get(key);
}