import { API_BASE_URL } from "./config"

export const fetchTaskCount = async (): Promise<number> => {
    await new Promise((resolve) => setTimeout(resolve, 3000));
    const resp = await fetch(`${API_BASE_URL}/tasks/count`)
    const data = await resp.json()
    return data.count
}
