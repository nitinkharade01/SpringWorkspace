import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client.js';

export default function TransactionsPage() {
  const [rows, setRows] = useState([]);
  useEffect(() => { api.get('/api/transactions?size=20').then(res => setRows(res.data.data.content || [])); }, []);
  return <div className="page"><h2>Transaction Management</h2><table><thead><tr><th>ID</th><th>Customer</th><th>Amount</th><th>Status</th><th>Risk</th></tr></thead><tbody>{rows.map(tx => <tr key={tx.transactionId}><td><Link to={`/transactions/${tx.transactionId}`}>{tx.transactionId}</Link></td><td>{tx.customerName}</td><td>{tx.currency} {tx.amount}</td><td>{tx.transactionStatus}</td><td>{tx.riskStatus}</td></tr>)}</tbody></table></div>;
}
