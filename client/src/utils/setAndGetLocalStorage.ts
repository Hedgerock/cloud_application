interface LocalStorageData<T> {
    value: T;
    expiry: number;

}

export const setLocalStorage = <T>(title: string, ttl: number, value: T) => {
    const now = Date.now();

    const item: LocalStorageData<T> = {
        value: value,
        expiry: now + ttl
    }

    localStorage.setItem(title, JSON.stringify(item));
}

export const removeFromLocalStorage = (key: string) => {
    localStorage.removeItem(key);
}

export const getLocalStorage = <T>(key:string) => {
    const item = localStorage.getItem(key);

    if (!item) return null;

    const result = JSON.parse(item) as LocalStorageData<T>;
    const now = Date.now();

    if (now > result.expiry) {
        removeFromLocalStorage(key);
        return null;
    }

    return result.value;
}