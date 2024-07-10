import { useTaskCount } from '../../lib/queries';

export default function TaskCount() {
    const { data, error, isLoading } = useTaskCount()

    if (isLoading) return <span>Loading...</span>
    if (error) return <span>Failed to load!</span>
    return <span>{data}</span>
}
