import { API_BASE_URL } from "./config"
import useSWR, { SWRResponse } from 'swr'

const fetcher = async <T>(path: string): Promise<T> => {
    const resp = await fetch(`${API_BASE_URL}${path}`)
    return await resp.json()
}

interface TaskCount {
    count: number
}

export const useTaskCount = (): SWRResponse<number, any, any> => {
    return useSWR<number>('/tasks/count', async (path) => {
        const resp = await fetcher<TaskCount>(path)
        return resp.count
    })
}
