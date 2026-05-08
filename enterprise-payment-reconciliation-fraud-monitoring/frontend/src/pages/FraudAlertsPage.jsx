import { useEffect, useState } from 'react';
import api from '../api/client.js';

export default function FraudAlertsPage() {
  const [alerts, setAlerts] = useState([]);
  useEffect(() => { api.get('/api/fraud-alerts').then(res => setAlerts(res.data.data)); }, []);
  return <div className="page"><h2>Fraud Alerts</h2><table><thead><tr><th>Transaction</th><th>Score</th><th>Risk</th><th>Status</th><th>Reason</th></tr></thead><tbody>{alerts.map(a => <tr key={a.id}><td>{a.transactionId}</td><td>{a.riskScore}</td><td>{a.riskStatus}</td><td>{a.alertStatus}</td><td>{a.fraudReason}</td></tr>)}</tbody></table></div>;
}
