import './ConnectionTracker.css';
import {useStartApp} from "../../../hooks/useStartApp.ts";

export const ConnectionTracker = () => {
    const { health, isFirstRender, isLoading } = useStartApp();

    if (isFirstRender.current || isLoading) {
        return null;
    }

    if (health.isOnline) {
        return null
    }

    return (
        <div className={ `no-connection-block` }>
            <h2>NO CONNECTION</h2>
        </div>
    )
}