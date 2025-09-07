import './App.css'
import 'nprogress/nprogress.css';
import {Navigate, Route, Routes} from "react-router-dom";
import {NotFoundPage} from "../pages/NotFoundPage.tsx";
import {StartingPage} from "../pages/StartingPage.tsx";
import {LoginPage} from "../pages/LoginPage.tsx";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {fetchCurrentUser} from "../redux/thunks/fetchCurrentUser.ts";
import type {AppDispatch, RootState} from "../redux/store/store.ts";
import {NonAuthBoundary} from "../hoc/boundaries/non_auth_boundary/NonAuthBoundary.tsx";

export const App = () => {
  const dispatch = useDispatch<AppDispatch>();
  const user = useSelector((state: RootState) => state.auth.user);

  useEffect(() => {
      dispatch(fetchCurrentUser())
  }, [dispatch, user])

  return (
      <Routes>
          <Route index element={<StartingPage />} />
          <Route path="/login" element={
              <NonAuthBoundary>
                  <LoginPage />
              </NonAuthBoundary>
          } />
          <Route path="/*" element={
              <Navigate to={"/not-found-page"} replace={true}/>
          }
          />
          <Route path="/not-found-page" element={ <NotFoundPage /> } />
      </Routes>
  )

}
