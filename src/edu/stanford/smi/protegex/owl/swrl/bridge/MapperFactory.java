
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.MapperException;
import edu.stanford.smi.protegex.owl.swrl.ddm.impl.RelationalMapper;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;

public class MapperFactory
{
  public static Mapper createMapper(SQWRLQueryEngine queryEngine) throws MapperException
  {
    return new RelationalMapper(queryEngine);
  } // getMapper
} // MapperFactory
