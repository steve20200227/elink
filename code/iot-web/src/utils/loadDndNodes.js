const loadDndNodes = (schema) => {
  const nodeGroups = [
    {
      label: '触发源',
      name: 'source',
      id: '1'
    },
    ...Object.entries(schema.source).map(([key, value]) => ({
      label: value.label,
      name: value.name,
      id: value.id,
      parent: '1',
      type: 'source',
      nodeType: value.nodeType,
      node: value,
      group: 'source'
    })),

    {
      label: '数据处理',
      name: 'operator',
      id: '2',
    },
    ...Object.entries(schema.operator).map(([key, value]) => ({
      label: value.label,
      name: value.name,
      id: value.id,
      parent: '2',
      type: 'operator',
      nodeType: value.nodeType,
      node: value,
      group: 'operator'
    })),

    {
      label: '输出动作',
      name: 'sink',
      id: '3',
    },
    ...Object.entries(schema.sink).map(([key, value]) => ({
      label: value.label,
      name: value.name,
      id: value.id,
      parent: '3',
      type: 'sink',
      nodeType: value.nodeType,
      node: value,
      group: 'sink'
    }))
  ]
  return nodeGroups
}

export default loadDndNodes
