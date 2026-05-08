import { useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client.js';

export default function RegisterPage() {
  const [form, setForm] = useState({ fullName: '', email: '', password: '', roles: ['FINANCE_USER'] });
  const [message, setMessage] = useState('');
  const submit = async (e) => {
    e.preventDefault();
    await api.post('/api/auth/register', form);
    setMessage('User registered successfully');
  };
  return <div className="auth-page"><form onSubmit={submit} className="auth-card"><h1>Register User</h1><input placeholder="Full name" onChange={e => setForm({ ...form, fullName: e.target.value })} /><input placeholder="Email" onChange={e => setForm({ ...form, email: e.target.value })} /><input type="password" placeholder="Password" onChange={e => setForm({ ...form, password: e.target.value })} /><select onChange={e => setForm({ ...form, roles: [e.target.value] })}><option>FINANCE_USER</option><option>AUDITOR</option><option>ADMIN</option></select><button>Register</button>{message && <p className="success">{message}</p>}<Link to="/login">Back to login</Link></form></div>;
}
