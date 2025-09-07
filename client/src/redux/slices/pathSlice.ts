import {createSlice, type PayloadAction} from "@reduxjs/toolkit";

interface IPrevLocation {
    prevPath: string;
    currentPath: string;
    pathHistory: string[];
}

const initialValue: IPrevLocation = {
    prevPath: "/",
    currentPath: "/",
    pathHistory: ["/"]
}

export const pathSlice = createSlice({
    name: "pathSlice",
    initialState: initialValue,
    reducers: {
        setPrevPath(state, action: PayloadAction<IPrevLocation>) {
            state.prevPath = action.payload.prevPath;
            state.currentPath = action.payload.currentPath;
            state.pathHistory = action.payload.pathHistory;
        },
        removeLastPath(state) {
            state.pathHistory.pop();
        },
        removeCurrentPath(state, action: PayloadAction<{ target: string }>) {
          state.pathHistory = state.pathHistory.filter(history => history !== action.payload.target);
        },
        clearLoginHistory(state) {
            state.pathHistory = state.pathHistory.filter(history => history !== "/login")
        }
    }
})

export const {setPrevPath, removeLastPath, clearLoginHistory, removeCurrentPath} = pathSlice.actions;
export default pathSlice.reducer;