const serializedExpr = (name, properties, record) => {
  const params = []
  for (const key in properties) {
    if (properties.hasOwnProperty(key)) {
      const element = properties[key]
      if (record && record[element.name]) {
        if (element.type === 'array') {
          params.push(`[${record[element.name].map(item => `'${item}'`).join(',')}]`)
        }
        params.push(record[element.name])
      } else {
        switch (element.type) {
          case 'number':
            params.push('0')
            break;
          case 'string':
            params.push('')
            break;
          case 'boolean':
            params.push('false')
            break;
          case 'array':
            params.push('[]')
            break;
          default:
            params.push(null)
            break;
        }

      }
    }
  }
  return `${name}(${params.join(',')})`
}

const getOutputs = (data, outputs) => {
  const result = []
  Object.entries(outputs).forEach(([key, value]) => {
    value.connections.forEach((connection) => {
      const beConnected = data[connection.node]
      result.push(beConnected.data.name + beConnected.id)
    })
  })
  return result
}

const serializedUpload = (data) => {
  const nodes = {}
  const topo = {
    sources: [],
    edges: {},
  }
  Object.entries(data).forEach(([key, value]) => {
    //必须使用节点名称+唯一id作为节点集合的key，否则一个规则中如果有两个相同的节点，其中一个节点信息会被覆盖
    let code = value.data.name + value.id;
    if (value.data.type === 'operator') {
      nodes[code] = {
        type: value.data.type,
        nodeType: value.data.nodeType,
        props: {
          expr: serializedExpr(value.data.name, value.data.properties, value.data.record),
          node: value,
        },
      }
    } else {
      nodes[code] = {
        type: value.data.type,
        nodeType: value.data.nodeType,
        props: {
          ...value.data.record,
          node: value,
        },
      }
    }
    if (value.data.type === 'source') {
      topo.sources.push(code)
    }
    if (value.outputs && value.data.type !== 'sink') {
      topo.edges[code] = getOutputs(data, value.outputs)
    }
  })
  return {
    graph: {
      nodes,
      topo,
    }
  }
}

export default serializedUpload
