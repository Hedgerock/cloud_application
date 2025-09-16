import {createSlice} from "@reduxjs/toolkit";

interface HealthState {
    isOnline: boolean
}

const initialValue: HealthState = {
    isOnline: false
}

export const healthSlice = createSlice({
    name: "healthSlice",
    initialState: initialValue,
    reducers: {
        changeState(state, action) {
            state.isOnline = action.payload.isOnline
        }
    }
})

export const { changeState } = healthSlice.actions;
export default healthSlice.reducer;