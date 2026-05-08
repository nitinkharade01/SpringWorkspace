export default function MetricCard({ label, value, icon }) {
  return <section className="metric"><i className={`bi ${icon}`} /><span>{label}</span><strong>{value}</strong></section>;
}
