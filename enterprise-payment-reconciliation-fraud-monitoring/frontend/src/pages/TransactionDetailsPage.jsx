import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api/client.js';

export default function TransactionDetailsPage() {
  const { id } = useParams();
  const [tx, setTx] = useState(null);
  useEffect(() => { api.get(`/api/transactions/${id}`).then(res => setTx(res.data.data)); }, [id]);
  if (!tx) return <p>Loading transaction...</p>;
  return <div className="page"><h2>{tx.transactionId}</h2><div className="details">{Object.entries(tx).map(([k, v]) => <p key={k}><span>{k}</span><strong>{String(v)}</strong></p>)}</div></div>;
}
