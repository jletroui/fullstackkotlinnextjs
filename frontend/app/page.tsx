import { Suspense } from 'react';
import TaskCount from './ui/tasks/taskcount';

export default function Page() {
  const Loading = <span>Loading...</span>
  return (
    <main>
      <h1>Hello, Next.js!</h1>
      <h2>Task count: <Suspense fallback={Loading}><TaskCount/></Suspense></h2>
    </main>
  )
}
