import express from 'express'
import cors from 'cors'

const app = express()
const port = process.env.PORT || 3000

app.use(cors())
app.use(express.json())

app.get('/api/health', (_req, res) => {
  res.json({ message: '后端连接成功！', status: 'ok' })
})

app.listen(port, () => {
  console.log(`Backend server running at http://localhost:${port}`)
})
