import { useState } from 'react';
import api from '../api/client.js';

export default function CreateTransactionPage() {
  const [form, setForm] = useState({ userId: 2, customerName: '', sourceAccount: '', destinationAccount: '', amount: '', currency: 'INR', paymentMode: 'UPI', remarks: '' });
  const [message, setMessage] = useState('');
  const update = e => setForm({ ...form, [e.target.name]: e.target.value });
  const submit = async e => { e.preventDefault(); const res = await api.post('/api/transactions', form); setMessage(`Created ${res.data.data.transactionId}`); };
  return <div className="page"><h2>Create Transaction</h2><form className="form-grid" onSubmit={submit}>{Object.keys(form).map(key => <input key={key} name={key} value={form[key]} placeholder={key} onChange={update} />)}<button>Create</button></form>{message && <p className="success">{message}</p>}</div>;
}
