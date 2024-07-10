import { fetchTaskCount } from '../../lib/queries';

export default async function TaskCount() {
    const count = await fetchTaskCount()
    return <span>{count}</span>
}