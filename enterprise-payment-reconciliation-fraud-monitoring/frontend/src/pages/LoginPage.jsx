import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function LoginPage() {
  const [email, setEmail] = useState('admin@payment.com');
  const [password, setPassword] = useState('Admin@123');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const submit = async (e) => {
    e.preventDefault();
    try {
      await login(email, password);
      navigate('/');
    } catch {
      setError('Invalid credentials or service unavailable');
    }
  };
  return <div className="auth-page"><form onSubmit={submit} className="auth-card"><h1>Enterprise Payment Monitor</h1><input value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" /><input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" />{error && <p className="error">{error}</p>}<button>Login</button><Link to="/register">Register user</Link></form></div>;
}
