import {AUTH_CRED_KEY} from "../../../utils/constants";
import {getLocalStorage} from "../../../utils/setAndGetLocalStorage.ts";

const ConfirmationEmailPage = () => {
    const user = getLocalStorage<{ email: string }>(AUTH_CRED_KEY);

    return (
        <div style={{display: "flex", flexDirection: "column", gap: ".5rem"}}>
            <div>
                <p>
                    Thanks for registration. The confirmation email has sent to <b>{ user?.email }</b>.
                </p>
            </div>
            <div>
                <p>
                    If you haven't received message, please check a junk folder
                </p>
            </div>
        </div>
    )
}

export default ConfirmationEmailPage;