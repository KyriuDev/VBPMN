package fr.inria.convecs.optimus.py_to_java.cadp_compliance._2025j;

import fr.inria.convecs.optimus.constants.Bpmn;
import fr.inria.convecs.optimus.constants.Constant;
import fr.inria.convecs.optimus.constants.Lnt;
import fr.inria.convecs.optimus.py_to_java.cadp_compliance.generics.BpmnTypesBuilderGeneric;
import fr.inria.convecs.optimus.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BpmnTypesBuilder extends BpmnTypesBuilderGeneric
{
	private static final String BPMN_TYPES =
















			"(* \"find_incf()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf (p: BPROCESS, mergeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case p" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var name: ID, nodes: NODES, flows: FLOWS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"proc (name, nodes, flows) -> return find_incf_nodes (nodes, mergeid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_nodes()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf_nodes (nodes: NODES, mergeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case nodes" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: NODES" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) + //Necessary for lnt "case" structure
			"cons (g (gateways), tl) -> return find_incf_gateways (gateways," +
			Constant.LINE_FEED +
			Utils.indent(62) +
			"mergeid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (i (initial), tl)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (f (finals), tl)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (t (tasks), tl) -> return find_incf_nodes (tl, mergeid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_gateways()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf_gateways (gateways: GATEWAYS, mergeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case gateways" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"ident: ID, pattern: GPATTERN, sort: GSORT, incf: IDS, outf: IDS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: GATEWAYS" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (gateway (ident, pattern, sort, incf, outf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == mergeid) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return find_incf_gateways (tl, mergeid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_active_tokens()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_active_tokens (activeflows:IDS, incf:IDS): Nat is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var tokens: IDS, count: Nat in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"tokens := inter (activeflows, incf);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"count := card (tokens);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"return count" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"is_merge_possible_v2()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*-------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*-----------------Check for merge with BPMN 1.x semantics-----------------*)" +
			Constant.LINE_FEED +
			"(*-------------------------------------------------------------------------*)" +
			Constant.DOUBLE_LINE_FEED +
			"function" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"is_merge_possible_v2 (p: BPROCESS, activeflows:IDS, mergeid:ID): Bool" +
			Constant.LINE_FEED +
			"is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"incf: IDS, inactiveincf: IDS, active_merge: Nat, visited: IDS," +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"result1: Bool" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"visited := nil;" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"(*----- just iterate through gateways instead of all nodes -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"incf := find_incf (p, mergeid);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"active_merge := find_active_tokens (activeflows, incf);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(2) +
			"(*----- check if all the incf have tokens -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"if (active_merge == card (incf)) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"(*----- first remove incf with active tokens -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"inactiveincf := remove_ids_from_set (activeflows, incf);" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"(*----- then check upstream for remaining flows -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"result1 := check_af_upstream (visited?, p, activeflows," +
			Constant.LINE_FEED +
			Utils.indent(39) +
			"inactiveincf);" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return result1" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"is_sync_done()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"is_sync_done (p: BPROCESS, activeflows, syncstore: IDS, mergeid:ID): Bool" +
			Constant.LINE_FEED +
			"is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var incf: IDS, activesync: IDS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"(*----- just iterate through gateways instead of all nodes -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"incf := find_incf (p, mergeid);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"activesync := inter (activeflows, incf);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"if (empty (activesync)) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return False" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"elsif (inter (activesync, syncstore) == activesync) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return False" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"is_merge_possible_par()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Merge check for parallel gateways -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"is_merge_possible_par (p:BPROCESS, syncstore: IDS, mergeid:ID): Bool" +
			Constant.LINE_FEED +
			"is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var incf, activesync: IDS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"(*----- just iterate through gateways instead of all nodes -----*)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"incf := find_incf (p, mergeid);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"if (inter (incf, syncstore) == incf) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"return False" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"check_af_upstream()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- finds all the upstream flows and checks for tokens -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"check_af_upstream(in out visited: IDS, p: BPROCESS, activeflows: IDS," +
			Constant.LINE_FEED +
			Utils.indent(21) +
			"incf: IDS): Bool" +
			Constant.LINE_FEED +
			"is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var count: Nat, result1: Bool, result2: Bool in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"case incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"var hd: ID, tl: IDS, upflow: IDS, source: ID in" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) +
			"cons(hd, tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"source := find_flow_source (p, hd);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(5) +
			"if (source == DummyId) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"elsif (member (source, visited)) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"result1 := check_af_upstream (visited?, p, activeflows," +
			Constant.LINE_FEED +
			Utils.indent(48) +
			"tl);" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"return result1" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"visited := insert (source, visited);" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"upflow := get_incf_by_id(p, source);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(6) +
			"if (upflow == nil) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(7) +
			"return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"end if;" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(6) +
			"count := find_active_tokens (activeflows, upflow);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(6) +
			"if (count == 0 of Nat) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(7) +
			"result1 := check_af_upstream (visited?, p," +
			Constant.LINE_FEED +
			Utils.indent(51) +
			"activeflows, upflow);" +
			Constant.LINE_FEED +
			Utils.indentLNT(7) +
			"result2 := check_af_upstream (visited?, p," +
			Constant.LINE_FEED +
			Utils.indent(51) +
			" activeflows, tl);" +
			Constant.LINE_FEED +
			Utils.indentLNT(7) +
			"return result1 and result2" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(7) +
			"return False" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"| nil -> return True" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"end case" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_flow_source()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_flow_source (bpmn: BPROCESS, flowid: ID): ID is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case bpmn" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var name: ID, nodes: NODES, flows: FLOWS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"proc (name, nodes, flows) -> return traverse_flows (flows, flowid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"traverse_flows()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function traverse_flows (flows: FLOWS, flowid:ID): ID is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var dummySource: ID in " +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"dummySource := DummyId;" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"case flows" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"var ident: ID, source: ID, target: ID, tl: FLOWS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) +
			"cons (flow (ident, source, target), tl) ->  " +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"if (ident == flowid) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"return source" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(6) +
			"return traverse_flows (tl, flowid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"| nil -> return dummySource" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"end case" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"get_incf_by_id()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- given a node id, gets its incoming flows -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function get_incf_by_id (p: BPROCESS, nodeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case p" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var name: ID, nodes: NODES, flows: FLOWS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"proc (name, nodes, flows) -> return traverse_nodes (nodes, nodeid)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"traverse_nodes()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Traverse across all nodes in search of the node -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function traverse_nodes (nodes: NODES, id: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case nodes" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: NODES, incf:IDS" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"cons (g (gateways), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) +
			"incf := traverse_gateways (gateways, id);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(4) +
			"if (nil == incf) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"return traverse_nodes(tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(5) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (i (initial), tl) -> return traverse_nodes (tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (f (finals), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"incf := traverse_finals(finals, id);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (nil == incf) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return traverse_nodes(tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (t (tasks), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"incf := traverse_tasks (tasks, id);" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (nil == incf) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return traverse_nodes (tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"traverse_gateways()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Find incf of gateways -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function traverse_gateways (gateways: GATEWAYS, id: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case gateways" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"ident: ID, pattern: GPATTERN, sort: GSORT, incf: IDS, outf: IDS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: GATEWAYS" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (gateway (ident, pattern, sort, incf, outf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return traverse_gateways (tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"traverse_finals()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Find incf of finals -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function traverse_finals (finals: FINALS, id: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case finals" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var ident: ID, incf: IDS, tl: FINALS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (final (ident, incf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return traverse_finals (tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"traverse_finals()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Find incf of taks -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function traverse_tasks (tasks: TASKS, id: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case tasks" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var ident: ID, incf: IDS, outf: IDS, tl: TASKS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (task (ident, incf, outf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return traverse_tasks(tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"remove_incf()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Remove Incoming flows from activetokens -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function remove_incf (bpmn: BPROCESS, activeflows: IDS, mergeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"var incf: IDS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"incf := get_incf_by_id (bpmn, mergeid);" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"return remove_ids_from_set (incf, activeflows)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end var" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"remove_sync()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"function remove_sync (bpmn: BPROCESS, syncstore: IDS, mergeid: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"return remove_incf (bpmn, syncstore, mergeid)" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"remove_ids_from_set()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Helper functions to remove a set of IDS from the set ----- *)" +
			Constant.DOUBLE_LINE_FEED +
			"function remove_ids_from_set (toremove:IDS, inputset: IDS): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"return minus (inputset, toremove) " +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_nodes_all()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*------------Another version of code for process node traversal------------*)" +
			Constant.LINE_FEED +
			"(*------------------Fix: Remove the code from final version-----------------*)" +
			Constant.LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.LINE_FEED +
			"(*--------------------------------------------------------------------------*)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Traverse across all nodes in search of the node -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf_nodes_all (nodes: NODES, id: ID): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case nodes" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: NODES" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) + Utils.indent( 2) +
			"cons (g (gateways), tl) -> return find_incf_gatewaysv2 (gateways," +
			Constant.LINE_FEED +
			Utils.indent(64) +
			"id, tl)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (i (initial), tl) -> return find_incf_nodes_all (tl, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (f (finals), tl) -> return find_incf_finals (finals, id, tl)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| cons (t (tasks), tl) -> return find_incf_tasks (tasks, id, tl)" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return nil" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_gatewaysv2()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Find incf of gateways -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"find_incf_gatewaysv2 (gateways: GATEWAYS, id: ID, nextnodes: NODES): IDS" +
			Constant.LINE_FEED +
			"is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case gateways" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"ident: ID, pattern: GPATTERN, sort: GSORT, incf: IDS, outf: IDS," +
			Constant.LINE_FEED +
			Utils.indentLNT(3) +
			"tl: GATEWAYS" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"in" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) + Utils.indent(2) +
			"cons (gateway (ident, pattern, sort, incf, outf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return find_incf_gatewaysv2 (tl, id, nextnodes)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return find_incf_nodes_all (nextnodes, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_finals()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*----- Find incf of finals -----*)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf_finals (finals: FINALS, id: ID, nextnodes: NODES): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case finals" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var ident: ID, incf: IDS, tl: FINALS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (final (ident, incf), tl) ->" +
			Constant.DOUBLE_LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return find_incf_finals (tl, id, nextnodes)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return find_incf_nodes_all (nextnodes, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +

			"(* \"find_incf_tasks()\" function *)" +
			Constant.DOUBLE_LINE_FEED +
			"(*-------- Find incf of taks ------------*)" +
			Constant.DOUBLE_LINE_FEED +
			"function find_incf_tasks (tasks: TASKS, id: ID, nextnodes: NODES): IDS is" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"case tasks" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"var ident: ID, incf: IDS, outf: IDS, tl: TASKS in" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) + Utils.indent(2) +
			"cons (task (ident, incf, outf), tl) ->" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"if (ident == id) then" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return incf" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"else" +
			Constant.LINE_FEED +
			Utils.indentLNT(4) + Utils.indent(2) +
			"return find_incf_tasks (tl, id, nextnodes)" +
			Constant.LINE_FEED +
			Utils.indentLNT(3) + Utils.indent(2) +
			"end if" +
			Constant.LINE_FEED +
			Utils.indentLNT(2) +
			"| nil -> return find_incf_nodes_all (nextnodes, id)" +
			Constant.LINE_FEED +
			Utils.indentLNT(1) +
			"end case" +
			Constant.LINE_FEED +
			"end function" +
			Constant.DOUBLE_LINE_FEED +
			Lnt.STANDARD_SEPARATOR +
			Constant.DOUBLE_LINE_FEED +
			"end module"
	;

	public BpmnTypesBuilder()
	{

	}

	public void dumpBpmnTypesFile()
	{
		final StringBuilder bpmnTypesBuilder = new StringBuilder();
		this.writeFilePreamble(bpmnTypesBuilder);
		this.writeModulePreamble(bpmnTypesBuilder);
		this.writeIdsLntType(bpmnTypesBuilder);
		this.writeFlowLntType(bpmnTypesBuilder);
		this.writeSetOfFlowsLntType(bpmnTypesBuilder);
		this.writeTaskLntType(bpmnTypesBuilder);
		this.writeSetOfTasksLntType(bpmnTypesBuilder);
		this.writeInitialEventLntType(bpmnTypesBuilder);
		this.writeEndEventLntType(bpmnTypesBuilder);
		this.writeSetOfEndEventsLntType(bpmnTypesBuilder);
		this.writeGatewayTypeLntType(bpmnTypesBuilder);
		this.writeGatewayPatternLntType(bpmnTypesBuilder);
		this.writeGatewayLntType(bpmnTypesBuilder);
		this.writeSetOfGatewaysLntType(bpmnTypesBuilder);
		this.writeNodeLntType(bpmnTypesBuilder);
		this.writeSetOfNodesLntType(bpmnTypesBuilder);
		this.writeBpmnProcessLntType(bpmnTypesBuilder);
		this.writeIsMergePossibleLntFunction(bpmnTypesBuilder);
		this.writeFindIncfLntFunction(bpmnTypesBuilder);

		final File bpmnTypesFile = new File(this.outputDirectory + File.separator + Bpmn.TYPES_FILENAME);
		final PrintWriter printWriter;

		try
		{
			printWriter = new PrintWriter(bpmnTypesFile);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		printWriter.print(bpmnTypesBuilder);
		printWriter.flush();
		printWriter.close();
	}

	//Private methods

	private void writeLntSeparation(final StringBuilder builder)
	{
		builder.append(Constant.DOUBLE_LINE_FEED)
				.append(Lnt.STANDARD_SEPARATOR)
				.append(Constant.DOUBLE_LINE_FEED);
	}

	private void writeFilePreamble(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.SPACE)
				.append("BPMN data types (FACS'16), necessary for encoding unbalanced workflows")
				.append(Constant.SPACE)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				//Second line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.SPACE)
				.append("AUTHOR: Gwen Salaun")
				.append(Constant.SPACE)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED);
	}

	private void writeModulePreamble(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.MODULE)
				.append(Constant.SPACE)
				.append(Bpmn.TYPES_LNT_MODULE)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.ID_MODULE_NAME)
				.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
				.append(Lnt.WITH)
				.append(Constant.SPACE)
				.append(Lnt.GET)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.LOWER_THAN_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.EQUALS_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.IS);

		this.writeLntSeparation(builder);
	}

	private void writeIdsLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("Set of BPMN Identifiers LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Utils.indentLNT(1))
				.append(Lnt.WITH)
				.append(Constant.SPACE)
				.append(Lnt.EQUALS_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.NOT_EQUALS_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_INTERSECTION)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_CARDINAL)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_EMPTY)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_MEMBER)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_INSERT)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_UNION)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_REMOVE)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_MINUS)
				.append(Constant.LINE_FEED)

				//Fifth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeFlowLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Sequence Flow LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.FLOW_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.FLOW_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.FLOW)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.IDENT_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.SOURCE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.TARGET)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfFlowsLntType(final StringBuilder builder)
	{
		builder	//First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Set of Sequence Flows LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.FLOW_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeTaskLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Task LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.TASK_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.TASK_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.TASK)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.IDENT_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.OUTGOING_FLOW)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfTasksLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Set of Tasks LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_TASKS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_TASKS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.TASK_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeInitialEventLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Initial Event LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.INITIAL_EVENT_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.INITIAL_EVENT_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.INITIAL)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.IDENT_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.OUTGOING_FLOW)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Utils.indent(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("Several outgoing flows (?)")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeEndEventLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN End Event LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.END_EVENT_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.END_EVENT_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.FINAL)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.IDENT_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Utils.indent(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("Several incoming flows (?)")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfEndEventsLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Set of End Events LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_END_EVENTS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.END_EVENT_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayTypeLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Gateway Type LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_TYPE_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.EXCLUSIVE_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.PARALLEL_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCLUSIVE_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayPatternLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Gateway Pattern LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_PATTERN_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.SPLIT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Gateway LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Bpmn.GATEWAY)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.IDENT_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.PATTERN)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.GATEWAY_PATTERN_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.SORT)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.GATEWAY_TYPE_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.OUTGOING_FLOW)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfGatewaysLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Set of Gateways LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_GATEWAYS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_GATEWAYS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.GATEWAY_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeNodeLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Generic Node LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.NODE_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.NODE_LNT_TYPE_CARDINAL)
				.append(Utils.indent(10))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("Could it be simpler?")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.INITIAL_NODES_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.INITIAL)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.INITIAL_EVENT_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Utils.indentLNT(1))
				.append(Bpmn.FINAL_NODES_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.FINALS)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				//Fifth line
				.append(Utils.indentLNT(1))
				.append(Bpmn.GATEWAYS_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.GATEWAYS)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_GATEWAYS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				//Sixth line
				.append(Utils.indentLNT(1))
				.append(Bpmn.TASKS_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.TASKS)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_TASKS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				//Seventh line
				.append(Constant.LINE_FEED)
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfNodesLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Set of Generic Nodes LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.NODE_LNT_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeBpmnProcessLntType(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("BPMN Process LNT Type")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				//Second line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append("Not the most optimized encoding for traversals")
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Third line
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Utils.indentLNT(1))
				.append(Bpmn.PROCESS_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.NAME_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.FLOWS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				//Fifth line
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeIsMergePossibleLntFunction(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("is_merge_possible() LNT Function")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.FUNCTION)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Bpmn.IS_MERGE_POSSIBLE_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COLON_AND_SPACE)
				.append(Lnt.BOOLEAN_TYPE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				//Fifth line
				.append(Utils.indentLNT(1))
				.append(Lnt.VAR)
				.append(Constant.SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_MERGE_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Lnt.NATURAL_NUMBER_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.STATUS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Lnt.BOOLEAN_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				//Sixth line
				.append(Utils.indentLNT(2))
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				//Seventh line
				.append(Utils.indentLNT(2))
				.append(Bpmn.ACTIVE_MERGE_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.FIND_ACTIVE_TOKENS_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				//Eighth line
				.append(Utils.indentLNT(2))
				.append(Lnt.IF)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.ACTIVE_MERGE_VARIABLE)
				.append(Lnt.SPACED_EQUALS_OPERATOR)
				.append(0)
				.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
				.append(Lnt.THEN)
				.append(Constant.LINE_FEED)

				//Ninth line
				.append(Utils.indentLNT(3))
				.append(Bpmn.STATUS_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Lnt.FALSE)
				.append(Constant.LINE_FEED)

				//Tenth line
				.append(Utils.indentLNT(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				//Eleventh line
				.append(Utils.indentLNT(3))
				.append(Bpmn.STATUS_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Lnt.TRUE)
				.append(Constant.LINE_FEED)

				//Twelfth line
				.append(Utils.indentLNT(2))
				.append(Lnt.END_IF)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				//Thirteenth line
				.append(Utils.indentLNT(2))
				.append(Lnt.RETURN)
				.append(Constant.SPACE)
				.append(Bpmn.STATUS_VARIABLE)
				.append(Constant.LINE_FEED)

				//Fourteenth line
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				//Fifteenth line
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfLntFunction(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf() LNT Function")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.FUNCTION)
				.append(Constant.SPACE)
				.append(Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				//Third line
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.LINE_FEED)

				//Fourth line
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.SPACE)
				.append(Bpmn.NAME_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.FLOWS_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				//Fifth line
				.append(Utils.indentLNT(3))
				.append(Bpmn.PROCESS_IDENTIFIER)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.NAME_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.FLOWS_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.RETURN)
				.append(Constant.SPACE)
				.append(Bpmn.FIND_INCOMING_FLOWS_NODES_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.LINE_FEED)

				//Sixth line
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)

				//Seventh line
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfNodesLntFunction(final StringBuilder builder)
	{
		builder //First line
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.COMMENTS_DASHES)
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_nodes() LNT Function")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append(Constant.COMMENTS_DASHES)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				//Second line
				.append(Lnt.FUNCTION)
				.append(Constant.SPACE)
				.append(Bpmn.FIND_INCOMING_FLOWS_NODES_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COLON_AND_SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)



				//N-th line
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}
}
