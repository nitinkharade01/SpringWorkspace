import { useEffect, useState } from 'react';
import { Bar, BarChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import api from '../api/client.js';
import MetricCard from '../components/MetricCard.jsx';

export default function DashboardPage() {
  const [data, setData] = useState(null);
  useEffect(() => { api.get('/api/reports/dashboard').then(res => setData(res.data.data)); }, []);
  const modes = data ? Object.entries(data.paymentModeDistribution).map(([name, value]) => ({ name, value })) : [];
  const risk = data ? Object.entries(data.fraudRisk).map(([name, value]) => ({ name, value })) : [];
  if (!data) return <p>Loading dashboard...</p>;
  return <div className="page"><h2>Dashboard</h2><div className="metrics"><MetricCard label="Total Transactions" value={data.totalTransactions} icon="bi-credit-card" /><MetricCard label="Successful" value={data.successCount} icon="bi-check2-circle" /><MetricCard label="Failed" value={data.failedCount} icon="bi-x-circle" /><MetricCard label="Pending Reconciliation" value={data.reconciliationPendingCount} icon="bi-hourglass" /><MetricCard label="Fraud Alerts" value={data.fraudCount} icon="bi-shield-exclamation" /></div><div className="grid two"><section><h3>Daily Transaction Volume</h3><ResponsiveContainer height={280}><BarChart data={data.dailyVolume}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="date" /><YAxis /><Tooltip /><Bar dataKey="volume" fill="#1f7a5a" /></BarChart></ResponsiveContainer></section><section><h3>Payment Modes</h3><ResponsiveContainer height={280}><PieChart><Pie data={modes} dataKey="value" nameKey="name">{modes.map((_, i) => <Cell key={i} fill={['#1f7a5a', '#3b82f6', '#f59e0b', '#ef4444'][i]} />)}</Pie><Tooltip /></PieChart></ResponsiveContainer></section></div><section><h3>Fraud Risk Chart</h3><div className="risk-row">{risk.map(item => <span key={item.name}>{item.name}: <strong>{item.value}</strong></span>)}</div></section><section><h3>Recent Transactions</h3><table><tbody>{data.recentTransactions.map(tx => <tr key={tx.transactionId}><td>{tx.transactionId}</td><td>{tx.customerName}</td><td>{tx.amount}</td><td>{tx.status}</td></tr>)}</tbody></table></section></div>;
}
