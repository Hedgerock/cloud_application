import {Navigate, Route, Routes} from "react-router-dom";
import {NonAuthBoundary} from "../../hoc/boundaries/non_auth_boundary/NonAuthBoundary.tsx";
import {Suspense} from "react";
import {
    LazyAuthTemplate,
    LazyConfirmationPage,
    LazyConfirmEmailPage,
    LazyForgotPasswordPage,
    LazyLoginPage,
    LazyNotFoundPage,
    LazyRegistrationPage, LazyRestorePassword,
    LazyStartingPage
} from "./lazyRoutes.ts";

export const AppRoutes = () => {

    return (
        <Routes>
            <Route index element={<LazyStartingPage />} />

            <Route path="auth" element={
                <NonAuthBoundary>
                    <Suspense fallback={<div>Loading....</div>}>
                        <LazyAuthTemplate />
                    </Suspense>
                </NonAuthBoundary>
            }
            >
                <Route path={"login"} element={<LazyLoginPage />} />
                <Route path={"register"} element={<LazyRegistrationPage />} />
                <Route path={"confirmation_email"} element={<LazyConfirmEmailPage />} />
                <Route path={"confirm"} element={<LazyConfirmationPage />} />
                <Route path={"forgot_password"} element={<LazyForgotPasswordPage />} />
                <Route path={"restore_password"} element={<LazyRestorePassword />} />
            </Route>

            <Route path="*" element={<Navigate to={"/not-found-page"} replace={true}/>} />
            <Route path="not-found-page" element={ <LazyNotFoundPage /> } />
        </Routes>
    )
}