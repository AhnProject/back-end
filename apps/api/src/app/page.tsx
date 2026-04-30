export default function Home() {
  return (
    <main style={{ fontFamily: "monospace", padding: "2rem" }}>
      <h1>ReeL-Trip API</h1>
      <p>Server is running.</p>
      <h2>Endpoints</h2>
      <ul>
        <li>POST /api/auth/signup</li>
        <li>POST /api/auth/login</li>
        <li>GET /api/documents/health</li>
        <li>GET /api/documents</li>
        <li>POST /api/documents</li>
        <li>POST /api/documents/search</li>
        <li>GET /api/documents/[id]</li>
        <li>PUT /api/documents/[id]</li>
        <li>DELETE /api/documents/[id]</li>
        <li>GET /api/recommend</li>
        <li>POST /api/recommend</li>
      </ul>
    </main>
  );
}
