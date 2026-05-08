import { useEffect, useState } from 'react';
import api from '../api/client.js';
export default function UsersPage() { const [users, setUsers] = useState([]); useEffect(() => { api.get('/api/auth/users').then(res => setUsers(res.data.data)); }, []); return <div className="page"><h2>User Management</h2><table><tbody>{users.map(u => <tr key={u.id}><td>{u.fullName}</td><td>{u.email}</td><td>{u.roles.join(', ')}</td><td>{u.active ? 'ACTIVE' : 'DISABLED'}</td></tr>)}</tbody></table></div>; }
