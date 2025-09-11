import './App.css'
import 'nprogress/nprogress.css';
import {useDispatch} from "react-redux";
import {useEffect} from "react";
import {fetchCurrentUser} from "../redux/thunks/fetchCurrentUser.ts";
import type {AppDispatch} from "../redux/store/store.ts";
import {AppRoutes} from "./routes/AppRoutes.tsx";

export const App = () => {
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
      dispatch(fetchCurrentUser())
  }, [dispatch])

  return <><AppRoutes /></>
}
