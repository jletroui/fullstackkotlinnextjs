import '@testing-library/jest-dom'
import { render, screen, waitFor } from '@testing-library/react'
import { useTaskCount } from '../app/lib/queries'
import Page from '../app/page'

jest.mock('../app/lib/queries', () => ({
  useTaskCount: jest.fn()
}))

describe('Page', () => {
  it('renders a heading', async () => {
    (useTaskCount as any).mockReturnValue({data: 3, error: null, isLoading: false})

    render(<Page />)

    await waitFor(() => {
      expect(screen.getByRole('heading', {level: 2})).toHaveTextContent('Task count: 3')
    })
  })
})
